package net.take5.commons.pojo.input.params;

import net.take5.commons.pojo.input.AbstractParams;

public class RemoveColumnParams extends AbstractParams
{
    /** Index de la ligne à retirer */
    private Integer line;

    public Integer getLine()
    {
        return this.line;
    }

    public void setLine(Integer column)
    {
        this.line = column;
    }
}
