<?php

namespace Tavros\DomainBundle\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * Profile
 */
class Profile
{
    /**
     * @var integer
     */
    private $profId;

    /**
     * @var float
     */
    private $profMaximumSpeed;

    /**
     * @var float
     */
    private $profAverageSpeed;

    /**
     * @var float
     */
    private $profSpeedAcumulator;

    /**
     * @var integer
     */
    private $profSpeedTimes;

    /**
     * @var float
     */
    private $profMaximumHeight;

    /**
     * @var float
     */
    private $profTotalLength;

    /**
     * @var \Tavros\DomainBundle\Entity\Users
     */
    private $profUser;


    /**
     * Get profId
     *
     * @return integer 
     */
    public function getProfId()
    {
        return $this->profId;
    }

    /**
     * Set profMaximumSpeed
     *
     * @param float $profMaximumSpeed
     * @return Profile
     */
    public function setProfMaximumSpeed($profMaximumSpeed)
    {
        $this->profMaximumSpeed = $profMaximumSpeed;

        return $this;
    }

    /**
     * Get profMaximumSpeed
     *
     * @return float 
     */
    public function getProfMaximumSpeed()
    {
        return $this->profMaximumSpeed;
    }

    /**
     * Set profAverageSpeed
     *
     * @param float $profAverageSpeed
     * @return Profile
     */
    public function setProfAverageSpeed($profAverageSpeed)
    {
        $this->profAverageSpeed = $profAverageSpeed;

        return $this;
    }

    /**
     * Get profAverageSpeed
     *
     * @return float 
     */
    public function getProfAverageSpeed()
    {
        return $this->profAverageSpeed;
    }

    /**
     * Set profSpeedAcumulator
     *
     * @param float $profSpeedAcumulator
     * @return Profile
     */
    public function setProfSpeedAcumulator($profSpeedAcumulator)
    {
        $this->profSpeedAcumulator = $profSpeedAcumulator;

        return $this;
    }

    /**
     * Get profSpeedAcumulator
     *
     * @return float 
     */
    public function getProfSpeedAcumulator()
    {
        return $this->profSpeedAcumulator;
    }

    /**
     * Set profSpeedTimes
     *
     * @param integer $profSpeedTimes
     * @return Profile
     */
    public function setProfSpeedTimes($profSpeedTimes)
    {
        $this->profSpeedTimes = $profSpeedTimes;

        return $this;
    }

    /**
     * Get profSpeedTimes
     *
     * @return integer 
     */
    public function getProfSpeedTimes()
    {
        return $this->profSpeedTimes;
    }

    /**
     * Set profMaximumHeight
     *
     * @param float $profMaximumHeight
     * @return Profile
     */
    public function setProfMaximumHeight($profMaximumHeight)
    {
        $this->profMaximumHeight = $profMaximumHeight;

        return $this;
    }

    /**
     * Get profMaximumHeight
     *
     * @return float 
     */
    public function getProfMaximumHeight()
    {
        return $this->profMaximumHeight;
    }

    /**
     * Set profTotalLength
     *
     * @param float $profTotalLength
     * @return Profile
     */
    public function setProfTotalLength($profTotalLength)
    {
        $this->profTotalLength = $profTotalLength;

        return $this;
    }

    /**
     * Get profTotalLength
     *
     * @return float 
     */
    public function getProfTotalLength()
    {
        return $this->profTotalLength;
    }

    /**
     * Set profUser
     *
     * @param \Tavros\DomainBundle\Entity\Users $profUser
     * @return Profile
     */
    public function setProfUser(\Tavros\DomainBundle\Entity\Users $profUser = null)
    {
        $this->profUser = $profUser;

        return $this;
    }

    /**
     * Get profUser
     *
     * @return \Tavros\DomainBundle\Entity\Users 
     */
    public function getProfUser()
    {
        return $this->profUser;
    }
}
