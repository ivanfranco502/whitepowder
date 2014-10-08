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
     * @var float
     */
    private $alerYPosition;

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
     * @param float $alerYPosition
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
     * @return float 
     */
    public function getAlerYPosition()
    {
        return $this->alerYPosition;
    }
    
    /**
     * @var \Tavros\DomainBundle\Entity\Users
     */
    private $alerUser;


    /**
     * Set alerUser
     *
     * @param \Tavros\DomainBundle\Entity\Users $alerUser
     * @return Alert
     */
    public function setAlerUser(\Tavros\DomainBundle\Entity\Users $alerUser = null)
    {
        $this->alerUser = $alerUser;

        return $this;
    }

    /**
     * Get alerUser
     *
     * @return \Tavros\DomainBundle\Entity\Users 
     */
    public function getAlerUser()
    {
        return $this->alerUser;
    }
    /**
     * @var \DateTime
     */
    private $alerDate;

    /**
     * @var integer
     */
    private $alerRead;


    /**
     * Set alerDate
     *
     * @param \DateTime $alerDate
     * @return Alert
     */
    public function setAlerDate($alerDate)
    {
        $this->alerDate = $alerDate;

        return $this;
    }

    /**
     * Get alerDate
     *
     * @return \DateTime 
     */
    public function getAlerDate()
    {
        return $this->alerDate;
    }

    /**
     * Set alerRead
     *
     * @param integer $alerRead
     * @return Alert
     */
    public function setAlerRead($alerRead)
    {
        $this->alerRead = $alerRead;

        return $this;
    }

    /**
     * Get alerRead
     *
     * @return integer 
     */
    public function getAlerRead()
    {
        return $this->alerRead;
    }
}
