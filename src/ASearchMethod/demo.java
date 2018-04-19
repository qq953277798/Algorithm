package ASearchMethod;

import sun.reflect.generics.tree.Tree;

import java.util.Scanner;
import java.util.TreeSet;

/**
 * a算法解决8数码问题
 * 评估函数f(x)定义为：
 * 从初始节点S0出发，约束地经过节点X到达目标节点Sg的所有路径中最小路径代价的估计值
 * f(x)=g(x)+h(x)
 * g(x)表示从初始节点S0到节点X的实际代价；
 * h(x)表示从X到目标节点Sg的最优路径的估计代价。
 * <p>
 * 本题中g(x) 显而易见的就是实际代价
 * h(x) 则定义为与最终状态不相同的个数
 */
class openItem {
    //当前状态
    String[][] item ;

}

public class demo {
    //初始
    String[][] start = new String[3][3];
    //终态
    String[][] end = new String[3][3];
    //open表

    public demo() {
        Scanner in = new Scanner(System.in);
        //初始化start表
        System.out.println("请每行输入三个数(例1,2,3)");
        System.out.println("开始初始化start表");
        for (int i = 0; i < 3; i++) {
            String tmp = in.nextLine();
            start[i] = tmp.split(",");
        }
        System.out.println("开始初始化end表");
        for (int i = 0; i <3 ; i++) {
            String tmp = in.nextLine();
            end[i] = tmp.split(",");
        }
        //初始化
    }

    public static void main(String[] args) {
        demo d = new demo();//初始化完成

    }

}
