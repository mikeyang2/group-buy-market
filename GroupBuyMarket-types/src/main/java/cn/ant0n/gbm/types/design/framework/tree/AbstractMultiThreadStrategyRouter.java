package cn.ant0n.gbm.types.design.framework.tree;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public abstract class AbstractMultiThreadStrategyRouter<T, D, R> implements StrategyHandler<T, D, R>, StrategyMapper<T, D, R> {

    /**
     * 多线程异步加载区
     * @param requestParameter 请求参数
     * @param dynamicContext 动态上下文
     */
    protected abstract void multiThread(T requestParameter, D dynamicContext) throws ExecutionException, InterruptedException, TimeoutException;

    /**
     * 业务受理区
     * @param requestParameter 请求参数
     * @param dynamicContext 动态上下文
     * @return 节点业务结果
     */
    protected abstract R doApply(T requestParameter, D dynamicContext) throws ExecutionException, InterruptedException, TimeoutException;

    @Override
    public R apply(T requestParameter, D dynamicContext) throws ExecutionException, InterruptedException, TimeoutException {
        multiThread(requestParameter, dynamicContext);
        return this.doApply(requestParameter, dynamicContext);
    }

    @Setter
    @Getter
    protected StrategyHandler<T, D, R> defaultStrategyHandler = StrategyHandler.DEFAULT;

    public R router(T requestParameter, D dynamicContext) throws ExecutionException, InterruptedException, TimeoutException {
        StrategyHandler<T, D, R> strategyHandler = get(requestParameter, dynamicContext);
        if(null != strategyHandler){
            return strategyHandler.apply(requestParameter, dynamicContext);
        }
        return defaultStrategyHandler.apply(requestParameter, dynamicContext);
    }
}
