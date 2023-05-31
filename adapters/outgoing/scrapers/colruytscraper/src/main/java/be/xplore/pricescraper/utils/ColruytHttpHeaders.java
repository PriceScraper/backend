package be.xplore.pricescraper.utils;

import java.util.HashMap;

public class ColruytHttpHeaders {
  public static HashMap<String, String> get() {
    var map = new HashMap<String, String>();
    map.put("X-CG-APIKey", "a8ylmv13-b285-4788-9e14-0f79b7ed2411");
    map.put("Cookie",
        "TS01461a64=016303f95536c8fa9c056cf7fb1dfcaa6ef14e125ce4877139ac4bf5dee9209a2981eef0df60e7dd0f0c04319ed958a28a3f384b3d; dtCookie=v_4_srv_5_sn_FF6256A6B98116C847CA404C43A58081_perc_100000_ol_0_mul_1_app-3Ab84fed97a8123cd5_0; TS019261f5=016303f955d3680475e13741dad867edf878f311b6fffaabb56c5ea5734fff774fd058bf0c75b97535c895681fde6fea0acf9a4335");
    map.put("Host", "apip.colruyt.be");
    map.put("User-Agent",
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36 Edg/113.0.1774.57");
    return map;
  }
}
