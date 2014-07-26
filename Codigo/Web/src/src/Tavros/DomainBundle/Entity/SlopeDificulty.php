<?php

namespace Tavros\DomainBundle\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * SlopeDificulty
 */
class SlopeDificulty
{
    /**
     * @var integer
     */
    private $sldiId;

    /**
     * @var string
     */
    private $sldiDescription;

    /**
     * @var string
     */
    private $sldiColor;


    /**
     * Get sldiId
     *
     * @return integer 
     */
    public function getSldiId()
    {
        return $this->sldiId;
    }

    /**
     * Set sldiDescription
     *
     * @param string $sldiDescription
     * @return SlopeDificulty
     */
    public function setSldiDescription($sldiDescription)
    {
        $this->sldiDescription = $sldiDescription;

        return $this;
    }

    /**
     * Get sldiDescription
     *
     * @return string 
     */
    public function getSldiDescription()
    {
        return $this->sldiDescription;
    }

    /**
     * Set sldiColor
     *
     * @param string $sldiColor
     * @return SlopeDificulty
     */
    public function setSldiColor($sldiColor)
    {
        $this->sldiColor = $sldiColor;

        return $this;
    }

    /**
     * Get sldiColor
     *
     * @return string 
     */
    public function getSldiColor()
    {
        return $this->sldiColor;
    }
    
    public function __toString() {
        return $this->sldiDescription;
    }
}
