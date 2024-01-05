import com.data.market.MarketData;
import com.data.market.MarketDataPoint;
import com.data.market.MarketDataSeries;

import java.util.List;
import java.util.Optional;

public class ChartTest {
    public static void main(String[] args) {
        MarketDataPoint point = new MarketDataPoint("bitcoin", "usd", 15);            // by name not id
        List<MarketDataPoint> list = MarketDataSeries.convert();
        System.out.println(list);
    }
}

