<?php

namespace Tavros\DomainBundle\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * Coordinate
 */
class Coordinate
{
    /**
     * @var integer
     */
    private $coorId;

    /**
     * @var \DateTime
     */
    private $coorCreatedDate;

    /**
     * @var float
     */
    private $coorX;

    /**
     * @var float
     */
    private $coorY;


    /**
     * Get coorId
     *
     * @return integer 
     */
    public function getCoorId()
    {
        return $this->coorId;
    }

    /**
     * Set coorCreatedDate
     *
     * @param \DateTime $coorCreatedDate
     * @return Coordinate
     */
    public function setCoorCreatedDate($coorCreatedDate)
    {
        $this->coorCreatedDate = $coorCreatedDate;

        return $this;
    }

    /**
     * Get coorCreatedDate
     *
     * @return \DateTime 
     */
    public function getCoorCreatedDate()
    {
        return $this->coorCreatedDate;
    }

    /**
     * Set coorX
     *
     * @param float $coorX
     * @return Coordinate
     */
    public function setCoorX($coorX)
    {
        $this->coorX = $coorX;

        return $this;
    }

    /**
     * Get coorX
     *
     * @return float 
     */
    public function getCoorX()
    {
        return $this->coorX;
    }

    /**
     * Set coorY
     *
     * @param float $coorY
     * @return Coordinate
     */
    public function setCoorY($coorY)
    {
        $this->coorY = $coorY;

        return $this;
    }

    /**
     * Get coorY
     *
     * @return float 
     */
    public function getCoorY()
    {
        return $this->coorY;
    }
}
