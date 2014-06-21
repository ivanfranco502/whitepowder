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
    private $geinSeasonTil;


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
     * Set geinSeasonTil
     *
     * @param string $geinSeasonTil
     * @return GeneralInformation
     */
    public function setGeinSeasonTil($geinSeasonTil)
    {
        $this->geinSeasonTil = $geinSeasonTil;

        return $this;
    }

    /**
     * Get geinSeasonTil
     *
     * @return string 
     */
    public function getGeinSeasonTil()
    {
        return $this->geinSeasonTil;
    }
}
