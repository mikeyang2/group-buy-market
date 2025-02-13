package cn.ant0n.gbm.domain.trade.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class GroupBuyMarketSuccessEntity {
    private List<UserEntity> userEntities;

    public GroupBuyMarketSuccessEntity() {
        userEntities = new ArrayList<UserEntity>();
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserEntity{
        private String userId;
        private String productId;
        private String outTradeNo;
    }

    public void add(String userId, String productId, String outTradeNo){
        UserEntity userEntity = new UserEntity(userId, productId, outTradeNo);
        userEntities.add(userEntity);
    }
}
