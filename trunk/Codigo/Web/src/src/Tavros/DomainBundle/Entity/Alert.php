<?php

namespace Tavros\DomainBundle\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * Alert
 */
class Alert
{
    /**
     * @var integer
     */
    private $alerId;

    /**
     * @var float
     */
    private $alerXPosition;

    /**
     * @var string
     */
    private $alerYPosition;

    /**
     * @var \Tavros\DomainBundle\Entity\Message
     */
    private $alerMessage;


    /**
     * Get alerId
     *
     * @return integer 
     */
    public function getAlerId()
    {
        return $this->alerId;
    }

    /**
     * Set alerXPosition
     *
     * @param float $alerXPosition
     * @return Alert
     */
    public function setAlerXPosition($alerXPosition)
    {
        $this->alerXPosition = $alerXPosition;

        return $this;
    }

    /**
     * Get alerXPosition
     *
     * @return float 
     */
    public function getAlerXPosition()
    {
        return $this->alerXPosition;
    }

    /**
     * Set alerYPosition
     *
     * @param string $alerYPosition
     * @return Alert
     */
    public function setAlerYPosition($alerYPosition)
    {
        $this->alerYPosition = $alerYPosition;

        return $this;
    }

    /**
     * Get alerYPosition
     *
     * @return string 
     */
    public function getAlerYPosition()
    {
        return $this->alerYPosition;
    }

    /**
     * Set alerMessage
     *
     * @param \Tavros\DomainBundle\Entity\Message $alerMessage
     * @return Alert
     */
    public function setAlerMessage(\Tavros\DomainBundle\Entity\Message $alerMessage = null)
    {
        $this->alerMessage = $alerMessage;

        return $this;
    }

    /**
     * Get alerMessage
     *
     * @return \Tavros\DomainBundle\Entity\Message 
     */
    public function getAlerMessage()
    {
        return $this->alerMessage;
    }
}
