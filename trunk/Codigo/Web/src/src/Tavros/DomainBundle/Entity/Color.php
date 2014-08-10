<?php

namespace Tavros\DomainBundle\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * Color
 */
class Color
{
    /**
     * @var integer
     */
    private $coloId;

    /**
     * @var string
     */
    private $coloDescription;

    /**
     * @var string
     */
    private $coloHexaCode;


    /**
     * Get coloId
     *
     * @return integer 
     */
    public function getColoId()
    {
        return $this->coloId;
    }

    /**
     * Set coloDescription
     *
     * @param string $coloDescription
     * @return Color
     */
    public function setColoDescription($coloDescription)
    {
        $this->coloDescription = $coloDescription;

        return $this;
    }

    /**
     * Get coloDescription
     *
     * @return string 
     */
    public function getColoDescription()
    {
        return $this->coloDescription;
    }

    /**
     * Set coloHexaCode
     *
     * @param string $coloHexaCode
     * @return Color
     */
    public function setColoHexaCode($coloHexaCode)
    {
        $this->coloHexaCode = $coloHexaCode;

        return $this;
    }

    /**
     * Get coloHexaCode
     *
     * @return string 
     */
    public function getColoHexaCode()
    {
        return $this->coloHexaCode;
    }
    
    public function __toString() {
        return $this->coloDescription;
    }
}
