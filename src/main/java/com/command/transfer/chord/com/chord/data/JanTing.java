package com.command.transfer.chord.com.chord.data;

import com.command.transfer.common.ErrorCode;
import com.command.transfer.common.Response;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * JianTing
 *
 * @author liulei
 * @date 2020/11/15
 */
public class JanTing {

    static OkHttpClient client = new OkHttpClient();
    public static Map map = new HashMap();

    public static Map getMap(Map map) {
        return map;
    }

    public void qwe(String s) {
        String[] split = s.split("；");
        String[] split1 = split[0].split(":");

        //获取主机Ip地址
        InetAddress localHost = null;
        try {
            localHost = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        String ip = localHost.getHostAddress();

        String url = "http://" + split1[0] + ":9527/transfer/getFile?fileName=" + split[2] + "&ip=" + ip;
        final Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        try {
            final okhttp3.Response response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        map.put(split[1], split[2]);
    }
}
