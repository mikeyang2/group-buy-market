package cn.ant0n.gbm.trigger.http;

import cn.ant0n.gbm.api.response.Response;
import cn.ant0n.gbm.domain.activity.service.dcc.IDccService;
import cn.ant0n.gbm.types.enums.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController()
@Slf4j
@RequestMapping("/gbm/backend/api/dcc")
public class DccController {

    @Resource
    private IDccService dccService;

    @RequestMapping(method = RequestMethod.GET, value = "/dcc_value_config")
    public Response<String> config(@RequestParam String path, @RequestParam String changeValue) {
        try{
            dccService.downgrade(path, changeValue);
            return Response.<String>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data("success").build();
        }catch (Exception e){
            return Response.<String>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(e.getMessage())
                    .data("fail").build();
        }
    }
}
