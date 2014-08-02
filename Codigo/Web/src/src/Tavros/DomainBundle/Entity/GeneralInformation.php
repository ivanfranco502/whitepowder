<?php

namespace Tavros\DomainBundle\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * GeneralInformation
 */
class GeneralInformation
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
     * @return GeneralInformation
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
     * @return GeneralInformation
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
     * @return GeneralInformation
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
     * @return GeneralInformation
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
     * @return GeneralInformation
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
     * @return GeneralInformation
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
     * @return GeneralInformation
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
     * @return GeneralInformation
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
}
