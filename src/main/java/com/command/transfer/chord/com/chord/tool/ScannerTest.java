package com.command.transfer.chord.com.chord.tool;

import com.command.transfer.chord.com.chord.data.Node;

import java.util.Scanner;


public class ScannerTest implements Runnable {
    public void run() {
        Scanner scan = new Scanner(System.in);
        while (true) {
            // nextLine��ʽ�����ַ���
            System.out.println("nextLine��ʽ���գ�");
            // �ж��Ƿ�������
            if (scan.hasNextLine()) {
                String str2 = scan.nextLine();
                Node.Instance().fingerTable.PringTable();
                System.out.println(Node.Instance().addr.HashCode() + " " + Node.Instance().addr.GetString() + " " + Node.Instance().predecessor.GetString() + " " + Node.Instance().successor.GetString());
            }
        }
    }
}
