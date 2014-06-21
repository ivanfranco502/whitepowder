<?php

namespace Tavros\DomainBundle\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * Slope
 */
class Slope
{
    /**
     * @var integer
     */
    private $slopId;

    /**
     * @var string
     */
    private $slopDescription;

    /**
     * @var integer
     */
    private $slopLength;

    /**
     * @var \Tavros\DomainBundle\Entity\GeneralInformation
     */
    private $slopGeneralInformation;

    /**
     * @var \Tavros\DomainBundle\Entity\SlopeDificulty
     */
    private $slopDificulty;


    /**
     * Get slopId
     *
     * @return integer 
     */
    public function getSlopId()
    {
        return $this->slopId;
    }

    /**
     * Set slopDescription
     *
     * @param string $slopDescription
     * @return Slope
     */
    public function setSlopDescription($slopDescription)
    {
        $this->slopDescription = $slopDescription;

        return $this;
    }

    /**
     * Get slopDescription
     *
     * @return string 
     */
    public function getSlopDescription()
    {
        return $this->slopDescription;
    }

    /**
     * Set slopLength
     *
     * @param integer $slopLength
     * @return Slope
     */
    public function setSlopLength($slopLength)
    {
        $this->slopLength = $slopLength;

        return $this;
    }

    /**
     * Get slopLength
     *
     * @return integer 
     */
    public function getSlopLength()
    {
        return $this->slopLength;
    }

    /**
     * Set slopGeneralInformation
     *
     * @param \Tavros\DomainBundle\Entity\GeneralInformation $slopGeneralInformation
     * @return Slope
     */
    public function setSlopGeneralInformation(\Tavros\DomainBundle\Entity\GeneralInformation $slopGeneralInformation = null)
    {
        $this->slopGeneralInformation = $slopGeneralInformation;

        return $this;
    }

    /**
     * Get slopGeneralInformation
     *
     * @return \Tavros\DomainBundle\Entity\GeneralInformation 
     */
    public function getSlopGeneralInformation()
    {
        return $this->slopGeneralInformation;
    }

    /**
     * Set slopDificulty
     *
     * @param \Tavros\DomainBundle\Entity\SlopeDificulty $slopDificulty
     * @return Slope
     */
    public function setSlopDificulty(\Tavros\DomainBundle\Entity\SlopeDificulty $slopDificulty = null)
    {
        $this->slopDificulty = $slopDificulty;

        return $this;
    }

    /**
     * Get slopDificulty
     *
     * @return \Tavros\DomainBundle\Entity\SlopeDificulty 
     */
    public function getSlopDificulty()
    {
        return $this->slopDificulty;
    }
}
