<?php

namespace Tavros\DomainBundle\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * UserCoordinate
 */
class UserCoordinate
{
    /**
     * @var integer
     */
    private $uscoId;

    /**
     * @var \DateTime
     */
    private $uscoUpdateDate;

    /**
     * @var \Tavros\DomainBundle\Entity\Coordinate
     */
    private $uscoCoordinate;

    /**
     * @var \Tavros\DomainBundle\Entity\Users
     */
    private $uscoUser;


    /**
     * Get uscoId
     *
     * @return integer 
     */
    public function getUscoId()
    {
        return $this->uscoId;
    }

    /**
     * Set uscoUpdateDate
     *
     * @param \DateTime $uscoUpdateDate
     * @return UserCoordinate
     */
    public function setUscoUpdateDate($uscoUpdateDate)
    {
        $this->uscoUpdateDate = $uscoUpdateDate;

        return $this;
    }

    /**
     * Get uscoUpdateDate
     *
     * @return \DateTime 
     */
    public function getUscoUpdateDate()
    {
        return $this->uscoUpdateDate;
    }

    /**
     * Set uscoCoordinate
     *
     * @param \Tavros\DomainBundle\Entity\Coordinate $uscoCoordinate
     * @return UserCoordinate
     */
    public function setUscoCoordinate(\Tavros\DomainBundle\Entity\Coordinate $uscoCoordinate = null)
    {
        $this->uscoCoordinate = $uscoCoordinate;

        return $this;
    }

    /**
     * Get uscoCoordinate
     *
     * @return \Tavros\DomainBundle\Entity\Coordinate 
     */
    public function getUscoCoordinate()
    {
        return $this->uscoCoordinate;
    }

    /**
     * Set uscoUser
     *
     * @param \Tavros\DomainBundle\Entity\Users $uscoUser
     * @return UserCoordinate
     */
    public function setUscoUser(\Tavros\DomainBundle\Entity\Users $uscoUser = null)
    {
        $this->uscoUser = $uscoUser;

        return $this;
    }

    /**
     * Get uscoUser
     *
     * @return \Tavros\DomainBundle\Entity\Users 
     */
    public function getUscoUser()
    {
        return $this->uscoUser;
    }
}
