<?php

namespace Tavros\DomainBundle\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * GeneralInformationDTO
 */
class GeneralInformationDTO
{
    /**
     * @var integer
     */
    private $geinId;

    /**
     * @var string
     */
    private $geinCenterName;

    /**
     * @var string
     */
    private $geinAmenities;

    /**
     * @var float
     */
    private $geinMaximumHeight;

    /**
     * @var float
     */
    private $geinMinimumHeight;

    /**
     * @var string
     */
    private $geinSeasonSince;

    /**
     * @var string
     */
    private $geinSeasonTill;

    /**
     * @var string
     */
    private $geinLocation;

    /**
     * @var string
     */
    private $geinDetails;

    /**
     * @var \Doctrine\Common\Collections\Collection
     */
    private $Schedules;

    /**
     * Constructor
     */
    public function __construct()
    {
        $this->Schedules = new \Doctrine\Common\Collections\ArrayCollection();
    }

    /**
     * Get geinId
     *
     * @return integer 
     */
    public function getGeinId()
    {
        return $this->geinId;
    }

    /**
     * Set geinCenterName
     *
     * @param string $geinCenterName
     * @return GeneralInformationDTO
     */
    public function setGeinCenterName($geinCenterName)
    {
        $this->geinCenterName = $geinCenterName;

        return $this;
    }

    /**
     * Get geinCenterName
     *
     * @return string 
     */
    public function getGeinCenterName()
    {
        return $this->geinCenterName;
    }

    /**
     * Set geinAmenities
     *
     * @param string $geinAmenities
     * @return GeneralInformationDTO
     */
    public function setGeinAmenities($geinAmenities)
    {
        $this->geinAmenities = $geinAmenities;

        return $this;
    }

    /**
     * Get geinAmenities
     *
     * @return string 
     */
    public function getGeinAmenities()
    {
        return $this->geinAmenities;
    }

    /**
     * Set geinMaximumHeight
     *
     * @param float $geinMaximumHeight
     * @return GeneralInformationDTO
     */
    public function setGeinMaximumHeight($geinMaximumHeight)
    {
        $this->geinMaximumHeight = $geinMaximumHeight;

        return $this;
    }

    /**
     * Get geinMaximumHeight
     *
     * @return float 
     */
    public function getGeinMaximumHeight()
    {
        return $this->geinMaximumHeight;
    }

    /**
     * Set geinMinimumHeight
     *
     * @param float $geinMinimumHeight
     * @return GeneralInformationDTO
     */
    public function setGeinMinimumHeight($geinMinimumHeight)
    {
        $this->geinMinimumHeight = $geinMinimumHeight;

        return $this;
    }

    /**
     * Get geinMinimumHeight
     *
     * @return float 
     */
    public function getGeinMinimumHeight()
    {
        return $this->geinMinimumHeight;
    }

    /**
     * Set geinSeasonSince
     *
     * @param string $geinSeasonSince
     * @return GeneralInformationDTO
     */
    public function setGeinSeasonSince($geinSeasonSince)
    {
        $this->geinSeasonSince = $geinSeasonSince;

        return $this;
    }

    /**
     * Get geinSeasonSince
     *
     * @return string 
     */
    public function getGeinSeasonSince()
    {
        return $this->geinSeasonSince;
    }

    /**
     * Set geinSeasonTill
     *
     * @param string $geinSeasonTill
     * @return GeneralInformationDTO
     */
    public function setGeinSeasonTill($geinSeasonTill)
    {
        $this->geinSeasonTill = $geinSeasonTill;

        return $this;
    }

    /**
     * Get geinSeasonTill
     *
     * @return string 
     */
    public function getGeinSeasonTill()
    {
        return $this->geinSeasonTill;
    }

    /**
     * Set geinLocation
     *
     * @param string $geinLocation
     * @return GeneralInformationDTO
     */
    public function setGeinLocation($geinLocation)
    {
        $this->geinLocation = $geinLocation;

        return $this;
    }

    /**
     * Get geinLocation
     *
     * @return string 
     */
    public function getGeinLocation()
    {
        return $this->geinLocation;
    }

    /**
     * Set geinDetails
     *
     * @param string $geinDetails
     * @return GeneralInformationDTO
     */
    public function setGeinDetails($geinDetails)
    {
        $this->geinDetails = $geinDetails;

        return $this;
    }

    /**
     * Get geinDetails
     *
     * @return string 
     */
    public function getGeinDetails()
    {
        return $this->geinDetails;
    }

    /**
     * Add Schedules
     *
     * @param \Tavros\DomainBundle\Entity\HourDay $schedules
     * @return GeneralInformationDTO
     */
    public function addSchedule(\Tavros\DomainBundle\Entity\HourDay $schedules)
    {
        $this->Schedules[] = $schedules;

        return $this;
    }

    /**
     * Remove Schedules
     *
     * @param \Tavros\DomainBundle\Entity\HourDay $schedules
     */
    public function removeSchedule(\Tavros\DomainBundle\Entity\HourDay $schedules)
    {
        $this->Schedules->removeElement($schedules);
    }

    /**
     * Get Schedules
     *
     * @return \Doctrine\Common\Collections\Collection 
     */
    public function getSchedules()
    {
        return $this->Schedules;
    }
    /**
     * @var \Tavros\DomainBundle\Entity\Coordinate
     */
    private $geinCoordinate;


    /**
     * Set geinCoordinate
     *
     * @param \Tavros\DomainBundle\Entity\Coordinate $geinCoordinate
     * @return GeneralInformationDTO
     */
    public function setGeinCoordinate(\Tavros\DomainBundle\Entity\Coordinate $geinCoordinate = null)
    {
        $this->geinCoordinate = $geinCoordinate;

        return $this;
    }

    /**
     * Get geinCoordinate
     *
     * @return \Tavros\DomainBundle\Entity\Coordinate 
     */
    public function getGeinCoordinate()
    {
        return $this->geinCoordinate;
    }
    /**
     * @var float
     */
    private $geinX;

    /**
     * @var float
     */
    private $geinY;


    /**
     * Set geinX
     *
     * @param float $geinX
     * @return GeneralInformationDTO
     */
    public function setGeinX($geinX)
    {
        $this->geinX = $geinX;

        return $this;
    }

    /**
     * Get geinX
     *
     * @return float 
     */
    public function getGeinX()
    {
        return $this->geinX;
    }

    /**
     * Set geinY
     *
     * @param float $geinY
     * @return GeneralInformationDTO
     */
    public function setGeinY($geinY)
    {
        $this->geinY = $geinY;

        return $this;
    }

    /**
     * Get geinY
     *
     * @return float 
     */
    public function getGeinY()
    {
        return $this->geinY;
    }
}
