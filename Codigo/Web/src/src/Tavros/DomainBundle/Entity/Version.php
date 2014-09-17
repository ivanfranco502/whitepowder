<?php

namespace Tavros\DomainBundle\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * Version
 */
class Version
{
    /**
     * @var integer
     */
    private $versId;

    /**
     * @var integer
     */
    private $versNumber;


    /**
     * Get versId
     *
     * @return integer 
     */
    public function getVersId()
    {
        return $this->versId;
    }

    /**
     * Set versNumber
     *
     * @param integer $versNumber
     * @return Version
     */
    public function setVersNumber($versNumber)
    {
        $this->versNumber = $versNumber;

        return $this;
    }

    /**
     * Get versNumber
     *
     * @return integer 
     */
    public function getVersNumber()
    {
        return $this->versNumber;
    }
}
