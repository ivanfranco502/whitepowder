<?php

namespace Tavros\WebBundle\Admin;

use Sonata\AdminBundle\Admin\Admin;
use Sonata\AdminBundle\Datagrid\ListMapper;
use Sonata\AdminBundle\Datagrid\DatagridMapper;
use Sonata\AdminBundle\Form\FormMapper;

class GeneralInformationAdmin extends Admin {

    protected $baseRouteName = 'generalinformation';
    protected $baseRoutePattern = 'generalinformation';
    
    protected $childAssociationMapping = 'coordinate';

    // Fields to be shown on create/edit forms
    protected function configureFormFields(FormMapper $formMapper) {
        $formMapper
                ->add('geinCenterName', 'text', array('label' => 'Center Name'))
                ->add('geinAmenities', 'text', array('label' => 'Amenities'))
                ->add('geinMaximumHeight', 'text', array('label' => 'Max. Height'))
                ->add('geinMinimumHeight', 'text', array('label' => 'Min. Height'))
                ->add('geinSeasonSince', 'text', array('label' => 'Season since'))
                ->add('geinSeasonTill', 'text', array('label' => 'Season till'))
                ->add('geinLocation', 'text', array('label' => 'Location'))
                ->add('geinX', null, array('label' => 'Coordenada X'))
                ->add('geinY', null, array('label' => 'Coordenada Y'))
                ->add('geinDetails', 'text', array('label' => 'Details'))
                ->add('Schedules', null, array('label' => 'Schedules'))
                ->add('Slopes', null, array('label' => 'Slopes'));
    }

    // Fields to be shown on filter forms
    protected function configureDatagridFilters(DatagridMapper $datagridMapper) {
        $datagridMapper
                ->add('geinCenterName')
                ->add('geinAmenities')
                ->add('geinMaximumHeight')
                ->add('geinMinimumHeight')
                ->add('geinSeasonSince')
                ->add('geinSeasonTill')
                ->add('geinLocation')
                ->add('geinDetails');
    }

    // Fields to be shown on lists
    protected function configureListFields(ListMapper $listMapper) {
        $listMapper
                ->addIdentifier("geinCenterName")
                //->add('geinCenterName')
                ->add('geinAmenities')
                ->add('geinMaximumHeight')
                ->add('geinMinimumHeight')
                ->add('geinSeasonSince')
                ->add('geinSeasonTill')
                ->add('geinLocation')
                ->add('geinDetails');
    }

}
