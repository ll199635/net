package com.command.transfer.chord.com.net;

import com.command.transfer.chord.com.chord.tool.Const;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client {
    static private Client instance;

    static public Client Instance() throws SocketException {
        if (instance == null) {
            instance = new Client();
            return instance;
        } else {
            return instance;
        }
    }

    DatagramSocket s;

    public Client() throws SocketException {
        s = new DatagramSocket(Const.PORT + 1);
    }

    public void finalize() {
        s.close();
    }

    public void send(String ip, int port, String massage) throws UnknownHostException {
        System.out.println("���� IP:" + ip + " port:" + port + " data:" + massage);
        byte[] bs = massage.getBytes();

        DatagramPacket dp = new DatagramPacket(bs, bs.length, InetAddress.getByName(ip), port);
        try {
            s.send(dp);
        } catch (IOException e) {
            System.out.println("����ʧ�ܣ� ");
            e.printStackTrace();
        }
    }
}
