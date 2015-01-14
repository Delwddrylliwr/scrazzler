/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run;

/**
 *
 * @author Tom
 */
public class Clock {
    private int min;         //minute
    private int sec;         //second
    private int maxMin;
    private int maxSec;

    //Khởi tạo
   public Clock()
    {
    }
    public Clock(int a, int b)
    {
        maxMin=min=a;
        maxSec=sec=b;
    }


    //Get và Set thuộc tính
    public void setMin(int a)
    {
        min=a;
    }
    public void setSec(int b)
    {
        sec=b;
    }
     public int getMin()
    {
        return min;
    }
    public int getSec()
    {
        return sec;
    }
     public int getMaxMin()
    {
        return maxMin;
    }
    public int getMaxSec()
    {
        return maxSec;
    }


    //Giảm đồng hồ
    public Clock decrease()
    {
        Clock temp=new Clock();
        if(sec==0)
        {
            sec=59;
            min=min-1;
        }
        else
        {
            sec=sec-1;
        }
        temp.min=min;
        temp.sec=sec;
        return temp;
    }


    //Hiển thị ra chuỗi
    @Override
    public String toString()
    {
        String temp;
        if(sec<10&&min>=10)
        {
            temp=min+":"+"0"+sec;
        }
        else if(min>=10&&sec>=10)
        {
            temp=min+":"+sec;
        }
        else if(min<10&&sec>=10)
        {
            temp="0"+min+":"+sec;
        }
        else
        {
            temp="0"+min+":"+"0"+sec;
        }
        return temp;
    }
}
