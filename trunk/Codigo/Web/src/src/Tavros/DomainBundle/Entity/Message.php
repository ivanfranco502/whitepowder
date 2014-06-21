<?php

namespace Tavros\DomainBundle\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * Message
 */
class Message
{
    /**
     * @var integer
     */
    private $messId;

    /**
     * @var \DateTime
     */
    private $messCreateddate;

    /**
     * @var \Tavros\DomainBundle\Entity\MessageType
     */
    private $messMessageType;

    /**
     * @var \Tavros\DomainBundle\Entity\Users
     */
    private $messUser;


    /**
     * Get messId
     *
     * @return integer 
     */
    public function getMessId()
    {
        return $this->messId;
    }

    /**
     * Set messCreateddate
     *
     * @param \DateTime $messCreateddate
     * @return Message
     */
    public function setMessCreateddate($messCreateddate)
    {
        $this->messCreateddate = $messCreateddate;

        return $this;
    }

    /**
     * Get messCreateddate
     *
     * @return \DateTime 
     */
    public function getMessCreateddate()
    {
        return $this->messCreateddate;
    }

    /**
     * Set messMessageType
     *
     * @param \Tavros\DomainBundle\Entity\MessageType $messMessageType
     * @return Message
     */
    public function setMessMessageType(\Tavros\DomainBundle\Entity\MessageType $messMessageType = null)
    {
        $this->messMessageType = $messMessageType;

        return $this;
    }

    /**
     * Get messMessageType
     *
     * @return \Tavros\DomainBundle\Entity\MessageType 
     */
    public function getMessMessageType()
    {
        return $this->messMessageType;
    }

    /**
     * Set messUser
     *
     * @param \Tavros\DomainBundle\Entity\Users $messUser
     * @return Message
     */
    public function setMessUser(\Tavros\DomainBundle\Entity\Users $messUser = null)
    {
        $this->messUser = $messUser;

        return $this;
    }

    /**
     * Get messUser
     *
     * @return \Tavros\DomainBundle\Entity\Users 
     */
    public function getMessUser()
    {
        return $this->messUser;
    }
}
