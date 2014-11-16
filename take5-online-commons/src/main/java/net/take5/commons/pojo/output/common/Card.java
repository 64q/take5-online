package net.take5.commons.pojo.output.common;

public class Card implements Comparable<Card>
{
    /** Valeur de la carte */
    private Integer value;

    /** Têtes de boeufs */
    private Integer oxHeads;

    public Integer getValue()
    {
        return this.value;
    }

    public void setValue(Integer value)
    {
        this.value = value;
    }

    public Integer getOxHeads()
    {
        return this.oxHeads;
    }

    public void setOxHeads(Integer oxHeads)
    {
        this.oxHeads = oxHeads;
    }

    @Override
    public int compareTo(Card o)
    {
        return this.value.compareTo(o.getValue());
    }

    @Override
    public String toString()
    {
        return "Card [value=" + this.value + ", oxHeads=" + this.oxHeads + "]";
    }
}
