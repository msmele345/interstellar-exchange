package com.mitchmele.interstellarexchange.services;

import com.mitchmele.interstellarexchange.ask.Ask;
import com.mitchmele.interstellarexchange.bid.Bid;
import com.mitchmele.interstellarexchange.quote.QuotePrice;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class QuoteConverterTest {

    @InjectMocks
    QuoteConverter quoteConverter;

    @Test
    public void transform_returnsBidFromJson() throws Exception {
        String incomingJson = "{\"id\":162,\"symbol\":\"OGC\",\"bidPrice\":23.05,\"timeStamp\":1591614219318,\"price\":23.05}";

        Bid expectedBid = Bid.builder()
                .id(162)
                .symbol("OGC")
                .bidPrice(BigDecimal.valueOf(23.05))
                .build();

        QuotePrice actual = quoteConverter.convert(incomingJson);
        assertThat(actual).isEqualToIgnoringGivenFields(expectedBid, "timeStamp");
    }

    @Test
    public void convert_returnsAskFromJson() {

        String incomingJson = "{\"id\":162,\"symbol\":\"OGC\",\"askPrice\":23.11,\"timeStamp\":1591614219318,\"price\":23.05}";

        Ask expectedAsk = Ask.builder()
                .id(162)
                .symbol("OGC")
                .askPrice(BigDecimal.valueOf(23.11))
                .build();

        QuotePrice actual = quoteConverter.convert(incomingJson);
        assertThat(actual).isEqualToIgnoringGivenFields(expectedAsk, "timeStamp");
    }

    @Test
    public void transform_throwsRuntimeException_givenBadJson() {
        String incomingBadJson = "{\"id\":162,\"symbol\":\"OGC\",\"askPrice\"asdfas:23.11,\"timeStamp\":1591614219318,\"price\":23.05}";

        assertThatThrownBy(() -> quoteConverter.convert(incomingBadJson))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Expected a ':' after a key at 36 [character 37 line 1]");
    }

    @Test
    public void convert_returnsNullIfIncomingJson_doesNotContainBidOrAskPrice() {
        String incomingJson = "{\"id\":162,\"symbol\":\"OGC\",\"someOtherPrice\":23.11,\"timeStamp\":1591614219318,\"price\":23.05}";

        QuotePrice actual = quoteConverter.convert(incomingJson);
        assertThat(actual).isNull();
    }
}