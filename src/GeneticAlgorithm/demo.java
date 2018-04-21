package GeneticAlgorithm;

import java.util.*;

/**
 * 遗传算法
 * 借鉴生物进化论，遗传算法将要解决的问题模拟成一个生物进化的过程
 * 通过复制、交叉、突变等操作产生下一代的解，并逐步淘汰掉适应度函数值低的解
 * 增加适应度函数值高的解。这样进化N代后就很有可能会进化出适应度函数值很高的个体。
 * 举个例子，使用遗传算法解决“0-1背包问题”的思路：
 * 0-1背包的解可以编码为一串0-1字符串（0：不取，1：取） ；
 * 首先，随机产生M个0-1字符串，然后评价这些0-1字符串作为0-1背包问题的解的优劣；
 * 然后，随机选择一些字符串通过交叉、突变等操作产生下一代的M个字符串，而且较优的解被选中的概率要比较高。
 * 这样经过G代的进化后就可能会产生出0-1背包问题的一个“近似最优解”。
 * <p>
 * 简化这个算法
 * 随机产生的解中 每次选取最优的前20%的解 生成新的20%的新个体 (选择)
 * 以pm的几率交叉
 * 以px的几率变异
 * 种群迭代的次数为5000
 * 用户提供参数 背包容量 rom
 * 物品表 goodsTable
 */
class Goods { //物品
    int value;//价值
    int weight;//重量

    public Goods(int value, int weight) {
        this.value = value;
        this.weight = weight;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}

class Individual implements Comparator<Individual> { //个体
    byte[] result ;//结果编码
    int fit;//适应度  越高越好

    public byte[] getResult() {
        return result;
    }

    public void setResult(byte[] result) {
        this.result = result;
    }

    public int getFit() {
        return fit;
    }

    public void setFit(int fit) {
        this.fit = fit;
    }

    @Override
    public int compare(Individual o1, Individual o2) {
        return o2.fit - o1.fit;
    }
}


public class demo {

    public static final int scale = 5000;//迭代次数
    public static final int populationNum = 100 ;//种群规模
    public static final double py = 0.1;//选择概率
    public static int rom;//背包容量
    public static double pm;//交叉概率
    public static double px;//变异概率
    public static ArrayList<Goods> goodsTable;//物品列表
    public static  Individual[] population = new Individual[populationNum];//种群(个体的数组)
    public static void init() {
        Scanner in = new Scanner(System.in);
        System.out.println("请输入背包容量");
        rom = in.nextInt();
        System.out.println("请输入交叉概论");
        pm = in.nextDouble();
        System.out.println("请输入变异概率");
        px = in.nextDouble();
        int value = 0;
        int weigit = 0;
        System.out.println("请输入物品列表 结束输入请输入(-1)");
        goodsTable = new ArrayList<>();
        while (true) {
            value = in.nextInt();
            if (value == -1) {
                break;
            }
            weigit = in.nextInt();
            goodsTable.add(new Goods(value, weigit));
        }
        //初始化种群
        initPopulation();
        System.out.println("初始化完成");

    }

    public static void initPopulation() {

        for (int i = 0; i < population.length; i++) {//将种群赋值
            //随机生成 和物品个数等长的编码
            Individual inv = new Individual();//创建一个个体
            byte[] result = new byte[goodsTable.size()];
            for (int j = 0; j < goodsTable.size(); j++) {
                result[j] = (byte) Math.round(Math.random());//随机生成
            }
            inv.setResult(result); // 设置编码
            inv.setFit(Fit(result));//返回适应度
            population[i] = inv;
        }
    }

    public static int Fit(byte[] result) {
        //计算适应度 适应度就是价值  当容量超出是价值为零
        int valueSum=0;//价值和
        int weightSum = 0;//重量和
        for (int i = 0; i < result.length; i++) {
            valueSum += result[i] * goodsTable.get(i).value;
            weightSum += result[i] * goodsTable.get(i).weight;
        }
        if (weightSum > rom)//超重 适应度为零
        {
            return 0;
        } else {
            return valueSum;//返回价值(适应度)
        }
    }

    public static void main(String[] args) {
        init();//初始化
        for (int i = 0; i < 5000; i++) {//按照迭代规模进行进化
            Arrays.sort(population, new Comparator<Individual>() {
                public int compare(Individual o1, Individual o2) {
                    return o2.fit-o1.fit;
                }
            });
            Individual[] tmp = new Individual[(int) (populationNum * py)];
            ArrCopy(tmp, population);//将前20拿出            //选择 就是把种群前20取出并两两配对
            //交叉 就是两两配对中按概率交换基因片段
            cross(tmp);
            //变异就是没一位每一个都按照概率
            variation(tmp);
            //将tmp拷贝到种群后
            for (int j = 0; j < tmp.length; j++) {
                population[population.length - 1 - j] = tmp[j];
            }
        }
        System.out.println("最大价值:"+population[0].fit);
        System.out.println("最大组合:"+Arrays.toString(population[0].result));
    }

    public static void variation(Individual[] tmp) {
        for (int i = 0; i < tmp.length; i++) {
            for (int j = 0; j < tmp[i].result.length; j++) {
                if (Math.random()<px) {
                    tmp[i].result[j] = (byte) (tmp[i].result[j] ^ 1);
                }
            }
        }
    }

    public static void cross(Individual[] tmp) {
        for (int i = 1; i < tmp.length; i++) {
            double RandomPm = Math.random();//随机一个0-1的数
            int num1 =(int) (Math.random() * goodsTable.size());
            int num2=(int) (Math.random() *  goodsTable.size());
            int start = num1 < num2 ? num1 : num2;
            int end = num1 > num2 ? num1 : num2;

            if (RandomPm < pm) {  //符合概率 则进行随机等长交换
                for (int j = start; j < end; j++) {//交换
                    byte t = tmp[i - 1].result[j];
                    tmp[i - 1].result[j] = tmp[i].result[j];
                    tmp[i].result[j] = t;
                }
            }
        }
    }

    public static void ArrCopy(Individual[] tmp, Individual[] population) {
        for (int i = 0; i < tmp.length; i++) {
            tmp[i] = population[i];
        }
    }
}
