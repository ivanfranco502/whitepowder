<?php

namespace Tavros\DomainBundle\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * Notification
 */
class Notification
{
    /**
     * @var integer
     */
    private $notiId;

    /**
     * @var string
     */
    private $notiDescription;

    /**
     * @var \Doctrine\Common\Collections\Collection
     */
    private $nousUser;

    /**
     * @var \Tavros\DomainBundle\Entity\NotificationType
     */
    private $notiNotificationType;
    
    /**
     * Constructor
     */
    public function __construct()
    {
        $this->nousUser = new \Doctrine\Common\Collections\ArrayCollection();
    }

    /**
     * Get notiId
     *
     * @return integer 
     */
    public function getNotiId()
    {
        return $this->notiId;
    }

    /**
     * Set notiDescription
     *
     * @param string $notiDescription
     * @return Notification
     */
    public function setNotiDescription($notiDescription)
    {
        $this->notiDescription = $notiDescription;

        return $this;
    }

    /**
     * Get notiDescription
     *
     * @return string 
     */
    public function getNotiDescription()
    {
        return $this->notiDescription;
    }

    /**
     * Add nousUser
     *
     * @param \Tavros\DomainBundle\Entity\Users $nousUser
     * @return Notification
     */
    public function addNousUser(\Tavros\DomainBundle\Entity\Users $nousUser)
    {
        $this->nousUser[] = $nousUser;

        return $this;
    }

    /**
     * Remove nousUser
     *
     * @param \Tavros\DomainBundle\Entity\Users $nousUser
     */
    public function removeNousUser(\Tavros\DomainBundle\Entity\Users $nousUser)
    {
        $this->nousUser->removeElement($nousUser);
    }

    /**
     * Get nousUser
     *
     * @return \Doctrine\Common\Collections\Collection 
     */
    public function getNousUser()
    {
        return $this->nousUser;
    }
    
    /**
     * Set notiNotificationType
     *
     * @param \Tavros\DomainBundle\Entity\NotificationType $notiNotificationType
     * @return Message
     */
    public function setNotiNotificationType(\Tavros\DomainBundle\Entity\NotificationType $notiNotificationType = null)
    {
        $this->notiNotificationType = $notiNotificationType;

        return $this;
    }

    /**
     * Get notiNotificationType
     *
     * @return \Tavros\DomainBundle\Entity\NotificationType 
     */
    public function getNotiNotificationType()
    {
        return $this->notiNotificationType;
    }
}
