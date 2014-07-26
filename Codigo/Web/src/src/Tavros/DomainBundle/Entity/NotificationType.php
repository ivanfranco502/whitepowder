<?php

namespace Tavros\DomainBundle\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * NotificationType
 */
class NotificationType
{
    /**
     * @var integer
     */
    private $notyId;

    /**
     * @var string
     */
    private $notyDescription;


    /**
     * Get notyId
     *
     * @return integer 
     */
    public function getNotyId()
    {
        return $this->notyId;
    }

    /**
     * Set notyDescription
     *
     * @param string $notyDescription
     * @return NotificationType
     */
    public function setNotyDescription($notyDescription)
    {
        $this->notyDescription = $notyDescription;

        return $this;
    }

    /**
     * Get notyDescription
     *
     * @return string 
     */
    public function getNotyDescription()
    {
        return $this->notyDescription;
    }
}
