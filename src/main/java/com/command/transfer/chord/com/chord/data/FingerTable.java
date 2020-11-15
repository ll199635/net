package com.command.transfer.chord.com.chord.data;


import com.command.transfer.chord.com.chord.tool.Const;
import com.command.transfer.chord.com.chord.tool.Tool;

public class FingerTable {
    private Finger[] table;

    FingerTable(Address addr) {
        table = new Finger[Const.RING_LOC_DIGIT];
        for (int i = 0; i < Const.RING_LOC_DIGIT; i++) {
            table[i] = new Finger(Tool.AddLocal(addr.HashCode(), Const.RING_LOC_LIST[i]), null, false);
        }
    }

    public void SetTable(int i, Address aimAddr, boolean isFind) {
        table[i].addr = aimAddr;
        table[i].isFind = isFind;
    }

    public Finger SearchWithHash(long hash) {
        int temp = 0;
        for (int i = 0; i < Const.RING_LOC_DIGIT; i++) {
            if (IsBehind(hash, table[i].local)) {
                temp = i;
            } else {
                break;
            }
        }
        if (table[temp].addr == null) {
            for (int i = temp; i >= 0; i--) {
                if (table[i].addr != null) {
                    temp = i;
                    break;
                }
            }
        }
        return table[temp];
    }

    public int GetTableNum(long hash) {
        int temp = 0;
        for (int i = 0; i < Const.RING_LOC_DIGIT; i++) {
            if (hash == table[i].local) {
                temp = i;
            }
        }
        return temp;
    }

    public long GetTableLoc(int i) {
        return table[i].local;
    }

    public void Update(Address aimAddr) {
        for (int i = 0; i < Const.RING_LOC_DIGIT; i++) {
            if ((IsBehind(aimAddr.HashCode(), table[i].local) && table[i].addr == null) ||
                    (IsBehind(aimAddr.HashCode(), table[i].local) && IsBehind(table[i].addr.HashCode(), aimAddr.HashCode()))) {
                table[i].addr = new Address(aimAddr.GetString());
            }
        }

    }

    private boolean IsBehind(long behind, long front) {
        if (behind < Node.Instance().addr.HashCode()) {
            behind += Const.MAX_RING_LOC - 1;
        }
        if (front < Node.Instance().addr.HashCode()) {
            front += Const.MAX_RING_LOC - 1;
        }
        return behind >= front;
    }

    public boolean IsTableFull() {
        for (int i = 0; i < Const.RING_LOC_DIGIT; i++) {
            if (table[i].addr == null && table[i].isFind == false) {
                return false;
            }
        }
        return true;
    }

    public void PringTable() {
        System.out.println("********************************************************************");
        for (int i = 0; i < Const.RING_LOC_DIGIT; i++) {
            if (table[i].addr != null) {
                System.out.println(table[i].local + " " + table[i].addr.HashCode());
            } else {
                System.out.println(table[i].local + " 0000000");
            }
        }
        System.out.println("********************************************************************");
    }
}
