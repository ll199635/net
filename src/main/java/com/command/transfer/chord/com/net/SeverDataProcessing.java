package com.command.transfer.chord.com.net;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.command.transfer.chord.com.chord.data.Node;

import java.lang.reflect.InvocationTargetException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;


public class SeverDataProcessing implements Runnable {
    public Queue<String> DataQueue = new LinkedList<String>();

    public void run() {
        while (true) {
            try {
                Thread.sleep(10);
                if (DataQueue.size() > 0) {
                    String data = DataQueue.poll();
                    this.DistributeData(data);
                }
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SocketException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void DistributeData(String data) throws UnknownHostException, SocketException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        System.out.println("�յ��� data: " + data);
        JSONObject json = JSON.parseObject(data);
        switch (PacketType.values()[json.getInteger("PacketType")]) {
            case FindLocWithCreateNode:
                Node.Instance().FinddLocWithCreateNode(json);
                break;
            case SuccessFindCreateNodeLoc:
                Node.Instance().SuccessFindCreateNodeLoc(json);
                break;
            case ChangeSuccessor:
                Node.Instance().ChangeSuccessor(json);
                break;
            case FindLoc:
                Node.Instance().FindLoc(json);
                break;
            case SuccessFindLoc:
                Node.Instance().SuccessFindLoc(json);
                break;
            case UpdateNode:
                Node.Instance().UpdateNode(json);
                break;
            case FindFileLoc:
                Node.Instance().FindFileLoc(json);
                break;
            case SuccessFindFileLoc:
                Node.Instance().SuccessFindFileLoc(json);
                break;
        }

    }
}