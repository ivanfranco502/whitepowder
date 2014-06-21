<?php

namespace Tavros\DomainBundle\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * MessageType
 */
class MessageType
{
    /**
     * @var integer
     */
    private $metyId;

    /**
     * @var string
     */
    private $metyDescription;


    /**
     * Get metyId
     *
     * @return integer 
     */
    public function getMetyId()
    {
        return $this->metyId;
    }

    /**
     * Set metyDescription
     *
     * @param string $metyDescription
     * @return MessageType
     */
    public function setMetyDescription($metyDescription)
    {
        $this->metyDescription = $metyDescription;

        return $this;
    }

    /**
     * Get metyDescription
     *
     * @return string 
     */
    public function getMetyDescription()
    {
        return $this->metyDescription;
    }
}
