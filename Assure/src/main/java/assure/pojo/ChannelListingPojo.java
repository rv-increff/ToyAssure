package assure.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static assure.pojo.TableConstants.*;

@Getter
@Setter
@Entity
@Table(indexes = {@Index(name = INDEX_CHANNEL_LISTING, columnList = "binId, globalSkuId")})
public class ChannelListingPojo extends AbstractPojo {
    @Id
    @TableGenerator(name = SEQ_CHANNEL_LISTING, initialValue = SEQ_INITIAL_VALUE)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = SEQ_CHANNEL_LISTING)
    private Long id;

    @Column(nullable = false)
    private Long channelId;

    @Column(nullable = false)
    private String channelSkuId;

    @Column(nullable = false)
    private Long clientId;

    @Column(nullable = false)
    private Long globalSkuId;
}
