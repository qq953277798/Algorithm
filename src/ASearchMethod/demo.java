package ASearchMethod;

import sun.reflect.generics.tree.Tree;

import java.util.*;

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
class ZeroXy {
    int x;
    int y;

    public ZeroXy(int xx, int yy) {
        this.x = xx;
        this.y = yy;
    }
}

public class demo {
    public static int idnum = 1;//计数器
    //初始
    String[][] start = new String[3][3];
    //终态
    String[][] end = new String[3][3];
    //open表
    ArrayList<Item> openTable;
    //close表
    ArrayList<Item> closeTable;

    Stack<Item> stackFind;
    public demo() {
        Scanner in = new Scanner(System.in);
        //初始化start表
        System.out.println("请每行输入三个数(例1,2,3)");
        System.out.println("开始初始化start表,请在9个数中保证有且只有一个0");
        for (int i = 0; i < 3; i++) {
            String tmp = in.nextLine();
            start[i] = tmp.split(",");
        }
        System.out.println("开始初始化end表");
        for (int i = 0; i <3 ; i++) {
            String tmp = in.nextLine();
            end[i] = tmp.split(",");
        }
        //初始化open表
        Item head = new Item(idnum++,start,0,"start",0,esPrice(start,end));
        openTable = new ArrayList<>();
        openTable.add(head);
        //初始化close表
        closeTable = new ArrayList<>();
        //初始化路径栈
        stackFind = new Stack<>();
    }

    public int esPrice(String[][] estart, String[][] eend) {//计算估计代价  当前状态与最终状态不同的个数就是代价
        int result = 0;
        for (int i = 0; i < estart.length; i++) {
            for (int j = 0; j < estart.length; j++) {
                if (!estart[i][j].equals(eend[i][j])) {
                    result++;
                }
            }
        }
        return result;
    }

    public void Find() {
        /**
         * 寻路函数
         * 1.将open表中最小的拿出 并将当前状态从open表删除
         * 2.比较与目的状态是否相同  相同返回路径
         *      否则进行扩展 并将所有新节点放入open表 (若open表中存在此节点 若代价短则更新)
         * 3. 并加入close表 .
         */
        while (!this.openTable.isEmpty()) {//open表不为空进行寻路  isEmpty() 当表为空返回true  否则返回false
            //将open最小的取出 并从open表中删除
            Item min = this.minItem();
            if (min == null) {
                System.out.println("寻找最小节点出现异常请处理");
                return;
            }
            //判断是否是寻找的状态
            if (esPrice(min.item, end)==0) { //代价为零则说明状态相同
                System.out.println("路径:");
                showFind(min);
                return ;
            }
            else{
                //不是最终状态 加入close表
                this.closeTable.add(min);
                //扩展新节点 分为4个方向
                String[] s1 = "上,下,左,右".split(",");
                //循环扩展四个方向
                for (int i = 0; i < s1.length; i++) {
                    Item newItem = addItem(s1[i],min);//获得新节点 传入参数动作 父节点
                    if (newItem == null) {//扩展失败则跳过本次 continue
                        continue;
                    }
                    if (this.openTable.contains(newItem)) {//当前节点 表中已存在 若代价短则更新
                        if (newItem.sum < this.openTable.get(this.openTable.indexOf(newItem)).sum) {//如果代价小于表中已存节点 则更新
                            this.openTable.remove(this.openTable.indexOf(newItem));//删除老状态
                            this.openTable.add(newItem);//添加新状态
                        }
                    } else {//全新节点直接添加
                        this.openTable.add(newItem);
                    }
                }
            }
        }
        return;
    }

    public void showFind(Item min) {
        Item now = min;//当前节点
        while (esPrice(now.item, start) != 0) {//当前栈不为start 继续循环
            stackFind.push(now);//进栈
            now = getFatherItem(now);
        }
        //显示初始start
        showArray(closeTable.get(0));
        while (!stackFind.empty()) {
            showArray(stackFind.pop());
        }
    }

    public Item getFatherItem(Item now) {
        Item result = null;
        Iterator<Item> it = closeTable.iterator();
        while (it.hasNext()) {
            Item tmp = it.next();
            if (tmp.id == now.fatherId) {//找到父节点
                result = tmp;
                break;
            }
        }
        return result;
    }

    public void showArray(Item it) {
        System.out.println("acter:"+it.acter);
        for (int i = 0; i <it.item.length ; i++) {
            System.out.print("[");
            for (int j = 0; j < it.item.length; j++) {
                if (j == 2) {
                    System.out.print(it.item[i][j]);
                    break;
                }
                System.out.print(it.item[i][j]+",");
            }
            System.out.println("]");
        }
        System.out.println("------------");
    }

    public Item addItem(String s, Item min) {
        Item result = null;
        ZeroXy point = getZeroXy(min);//获取父状态 0坐标
        if (point == null) {
            System.out.println("父节点 0 位置未获取 请检查程序");
            return result;
        }
        switch (s) {
            case "左":
                //向左扩展 需要判断左是否越界
                if (point.y - 1 < 0) {//越界直接跳出
                    break;
                }else{//没越界的话进行状态改变
                    String[][] Old = copy(min.item);
                    //进行移动
                    String temp="";
                    temp = Old[point.x][point.y - 1];
                    Old[point.x][point.y - 1] = Old[point.x][point.y];
                    Old[point.x][point.y] = temp;
                    result = new Item(idnum++, Old, min.id, s, min.price + 1, esPrice(Old, end));
                    break;
                }

            case "右":
                //向右扩展 需要判断右是否越界
                if (point.y + 1 >2) {//越界直接跳出
                    break;
                }else{//没越界的话进行状态改变
                    String[][] Old = copy(min.item);
                    //进行移动
                    String temp="";
                    temp = Old[point.x][point.y + 1];
                    Old[point.x][point.y + 1] = Old[point.x][point.y];
                    Old[point.x][point.y] = temp;
                    result = new Item(idnum++, Old, min.id, s, min.price + 1, esPrice(Old, end));
                    break;
                }
            case "上":
                //向上扩展 需要判断上是否越界
                if (point.x - 1 < 0) {//越界直接跳出
                    break;
                }else{//没越界的话进行状态改变
                    String[][] Old = copy(min.item);
                    //进行移动
                    String temp="";
                    temp = Old[point.x-1][point.y];
                    Old[point.x-1][point.y ] = Old[point.x][point.y];
                    Old[point.x][point.y] = temp;
                    result = new Item(idnum++, Old, min.id, s, min.price + 1, esPrice(Old, end));
                    break;
                }
            case "下":
                //向下扩展 需要判断右是否越界
                if (point.x + 1 >2) {//越界直接跳出
                    break;
                }else{//没越界的话进行状态改变
                    String[][] Old = copy(min.item);
                    //进行移动
                    String temp="";
                    temp = Old[point.x+ 1][point.y ];
                    Old[point.x+1][point.y ] = Old[point.x][point.y];
                    Old[point.x][point.y] = temp;
                    result = new Item(idnum++, Old, min.id, s, min.price + 1, esPrice(Old, end));
                    break;
                }
        }
        return result;
    }

    public String[][] copy(String[][] item) {
        String[][] result = new String[3][3];
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result.length; j++) {
                result[i][j] = item[i][j];
            }
        }
        return result;
    }

    public ZeroXy getZeroXy(Item min) {
        ZeroXy result = null;
        for (int i = 0; i <3; i++) {
            for (int j = 0; j < 3; j++) {
                if (min.item[i][j].equals("0")) {//找到0
                    result = new ZeroXy(i, j);//记录坐标
                    break;//退出循环
                }
            }
        }
        return result;
    }

    public Item minItem() {
        Item min = null;
        int price = Integer.MAX_VALUE;
        Iterator<Item> it = this.openTable.iterator();
        while (it.hasNext()) {
            Item tmp = it.next();
            min = (tmp.sum < price) ? tmp : min;//记录最小的
            price = tmp.sum;//更新最小值
        }
        this.openTable.remove(min);//从open表中移除
        return min;
    }

    public static void main(String[] args) {
        demo d = new demo();//初始化完成
        d.Find();

    }

}
