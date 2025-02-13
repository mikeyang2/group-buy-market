package cn.ant0n.gbm.config;

import cn.ant0n.gbm.types.annotations.DCCValue;
import cn.ant0n.gbm.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@Slf4j
public class DCCValueFactoryConfig implements BeanPostProcessor {

    private Map<String, Object> dccValueBeanGroup = new ConcurrentHashMap<>();

    private final String DCC_VALUE_CONFIG_BASE_PATH = "gbm:dcc:";
    private final String DCC_VALUE_TOPIC = "gbm:dcc_topic";

    @Resource
    private RedissonClient redissonClient;

    public DCCValueFactoryConfig() {

    }

    @Bean("dccValueTopic")
    public RTopic topic(){
        RTopic topic = redissonClient.getTopic(DCC_VALUE_TOPIC);
        topic.addListener(String.class, ((charSequence, s) -> {
            String[] split = s.split(Constants.SPLIT);
            if(split.length != 2) return;
            String dccPath = DCC_VALUE_CONFIG_BASE_PATH.concat(split[0]);
            String changeValue = split[1];
            log.info("拼团系统-接收到动态变更, path:{}, changeValue:{}", dccPath, changeValue);
            Object bean = dccValueBeanGroup.get(dccPath);
            RBucket<String> bucket = redissonClient.getBucket(dccPath);
            if(!bucket.isExists()) return;
            String oldValue = bucket.get();
            bucket.set(changeValue);

            Object targetBeanObject = bean;
            Class<?> targetBeanClazz = bean.getClass();
            if(AopUtils.isAopProxy(targetBeanObject)){
                targetBeanClazz = AopUtils.getTargetClass(bean);
                targetBeanObject = AopProxyUtils.getSingletonTarget(bean);
            }
            Field[] fields = targetBeanClazz.getDeclaredFields();
            for(Field field : fields){
                if(field.isAnnotationPresent(DCCValue.class)){
                    DCCValue dccValue = field.getAnnotation(DCCValue.class);
                    String path = dccValue.path();
                    if(split[0].equals(path)){
                        try{
                            field.setAccessible(true);
                            field.set(targetBeanObject, changeValue);
                            field.setAccessible(false);
                            log.info("拼团系统-变更成功, path:{}, oldValue:{}-->changeValue:{}", dccPath, oldValue, changeValue);
                            break;
                        }catch (Exception e){
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }));
        return topic;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetBeanClazz = bean.getClass();
        Object targetBeanObject = bean;
        if(AopUtils.isAopProxy(targetBeanObject)){
            targetBeanClazz = AopUtils.getTargetClass(bean);
            targetBeanObject = AopProxyUtils.getSingletonTarget(bean);
        }
        Field[] fields = targetBeanClazz.getDeclaredFields();
        for (Field field : fields) {
            if(!field.isAnnotationPresent(DCCValue.class)) continue;
            DCCValue dccValue = field.getAnnotation(DCCValue.class);
            String defaultValue = dccValue.value();
            String path = dccValue.path();
            if(StringUtils.isBlank(defaultValue) || StringUtils.isBlank(path)){
                throw new RuntimeException("dcc值配置错误, bean.field:" + beanName + field.getName());
            }
            String dccPath = DCC_VALUE_CONFIG_BASE_PATH.concat(path);
           try{
               RBucket<String> bucket = redissonClient.getBucket(dccPath);
               if(bucket.isExists()){
                   String currValue = bucket.get();
                   field.setAccessible(true);
                   field.set(targetBeanObject, currValue);
                   field.setAccessible(false);
               }
               else{
                   bucket.set(defaultValue);
                   field.setAccessible(true);
                   field.set(targetBeanObject, defaultValue);
                   field.setAccessible(false);
               }
           }catch (Exception e){
               throw new RuntimeException(e);
           }
           dccValueBeanGroup.put(dccPath, targetBeanObject);
        }
        return bean;
    }
}
