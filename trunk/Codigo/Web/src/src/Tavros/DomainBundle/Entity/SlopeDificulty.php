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
     * @var \Tavros\DomainBundle\Entity\Color
     */
    private $sldiColor;


     public function __toString() {
        return $this->sldiDescription;
    } 
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
     * @param \Tavros\DomainBundle\Entity\Color $sldiColor
     * @return SlopeDificulty
     */
    public function setSldiColor(\Tavros\DomainBundle\Entity\Color $sldiColor = null)
    {
        $this->sldiColor = $sldiColor;

        return $this;
    }

    /**
     * Get sldiColor
     *
     * @return \Tavros\DomainBundle\Entity\Color 
     */
    public function getSldiColor()
    {
        return $this->sldiColor;
    }
}
