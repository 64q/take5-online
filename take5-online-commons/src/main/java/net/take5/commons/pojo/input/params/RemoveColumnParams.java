package net.take5.commons.pojo.input.params;

import net.take5.commons.pojo.input.AbstractParams;

public class RemoveColumnParams extends AbstractParams
{
    /** Index de la colonne à retirer */
    private Integer column;

    public Integer getColumn()
    {
        return this.column;
    }

    public void setColumn(Integer column)
    {
        this.column = column;
    }
}
