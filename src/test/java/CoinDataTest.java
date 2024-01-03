<<<<<<< HEAD
import com.data.coin.CoinData;
=======
import com.data.api.ApiClient;
import com.data.api.CoinData;
import com.data.api.CoinDataList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;
>>>>>>> acca7d816116538d9b3c20abe92bd139e78cf19c

public class CoinDataTest {
    public static void main(String[] args) {

        CoinData coinData = new CoinData("doge",true);
        System.out.println(coinData);
    }
}
