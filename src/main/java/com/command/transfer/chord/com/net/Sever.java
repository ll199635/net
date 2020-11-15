package com.command.transfer.chord.com.net;

import com.command.transfer.chord.com.chord.tool.Const;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;


public class Sever implements Runnable {
    DatagramSocket ds;
    SeverDataProcessing dataProcessing;

    private ArrayList<DatagramPacket> DataList = new ArrayList<DatagramPacket>();

    public Sever() throws Exception {
        ds = new DatagramSocket(Const.PORT);

        dataProcessing = new SeverDataProcessing();
        Thread t = new Thread(dataProcessing);
        t.start();
    }

    public void run() {
        while (true) {
            try {
                byte[] bbuf = new byte[1024];
                DatagramPacket dp = new DatagramPacket(bbuf, bbuf.length);
                ;
                ds.receive(dp);
                dataProcessing.DataQueue.offer(new String(dp.getData()));
                System.out.println("??? ip:" + dp.getAddress().getHostAddress() + " port:" + dp.getPort());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}