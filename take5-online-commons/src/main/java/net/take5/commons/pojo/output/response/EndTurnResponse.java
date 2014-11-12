package net.take5.commons.pojo.output.response;

public class EndTurnResponse extends NotificationResponse
{
    /** Choix automatique de la carte */
    private Boolean autoChoice;

    /** Choix de la carte */
    private Integer card;

    public Integer getCard()
    {
        return this.card;
    }

    public void setCard(Integer card)
    {
        this.card = card;
    }

    public Boolean getAutoChoice()
    {
        return this.autoChoice;
    }

    public void setAutoChoice(Boolean autoChoice)
    {
        this.autoChoice = autoChoice;
    }
}
