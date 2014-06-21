<?php

namespace Tavros\DomainBundle\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * SlopeCoordinate
 */
class SlopeCoordinate
{
    /**
     * @var integer
     */
    private $slcoId;

    /**
     * @var \DateTime
     */
    private $slcoUpdateDate;

    /**
     * @var \Tavros\DomainBundle\Entity\Coordinate
     */
    private $slcoCoordinate;

    /**
     * @var \Tavros\DomainBundle\Entity\Slope
     */
    private $slcoSlope;

    /**
     * @var \Tavros\DomainBundle\Entity\Users
     */
    private $slcoUser;


    /**
     * Get slcoId
     *
     * @return integer 
     */
    public function getSlcoId()
    {
        return $this->slcoId;
    }

    /**
     * Set slcoUpdateDate
     *
     * @param \DateTime $slcoUpdateDate
     * @return SlopeCoordinate
     */
    public function setSlcoUpdateDate($slcoUpdateDate)
    {
        $this->slcoUpdateDate = $slcoUpdateDate;

        return $this;
    }

    /**
     * Get slcoUpdateDate
     *
     * @return \DateTime 
     */
    public function getSlcoUpdateDate()
    {
        return $this->slcoUpdateDate;
    }

    /**
     * Set slcoCoordinate
     *
     * @param \Tavros\DomainBundle\Entity\Coordinate $slcoCoordinate
     * @return SlopeCoordinate
     */
    public function setSlcoCoordinate(\Tavros\DomainBundle\Entity\Coordinate $slcoCoordinate = null)
    {
        $this->slcoCoordinate = $slcoCoordinate;

        return $this;
    }

    /**
     * Get slcoCoordinate
     *
     * @return \Tavros\DomainBundle\Entity\Coordinate 
     */
    public function getSlcoCoordinate()
    {
        return $this->slcoCoordinate;
    }

    /**
     * Set slcoSlope
     *
     * @param \Tavros\DomainBundle\Entity\Slope $slcoSlope
     * @return SlopeCoordinate
     */
    public function setSlcoSlope(\Tavros\DomainBundle\Entity\Slope $slcoSlope = null)
    {
        $this->slcoSlope = $slcoSlope;

        return $this;
    }

    /**
     * Get slcoSlope
     *
     * @return \Tavros\DomainBundle\Entity\Slope 
     */
    public function getSlcoSlope()
    {
        return $this->slcoSlope;
    }

    /**
     * Set slcoUser
     *
     * @param \Tavros\DomainBundle\Entity\Users $slcoUser
     * @return SlopeCoordinate
     */
    public function setSlcoUser(\Tavros\DomainBundle\Entity\Users $slcoUser = null)
    {
        $this->slcoUser = $slcoUser;

        return $this;
    }

    /**
     * Get slcoUser
     *
     * @return \Tavros\DomainBundle\Entity\Users 
     */
    public function getSlcoUser()
    {
        return $this->slcoUser;
    }
}
