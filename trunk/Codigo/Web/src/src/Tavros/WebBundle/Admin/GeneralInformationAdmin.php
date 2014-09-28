<?php

namespace Tavros\WebBundle\Admin;

use Sonata\AdminBundle\Admin\Admin;
use Sonata\AdminBundle\Datagrid\ListMapper;
use Sonata\AdminBundle\Datagrid\DatagridMapper;
use Sonata\AdminBundle\Form\FormMapper;
use Sonata\AdminBundle\Route\RouteCollection;

class GeneralInformationAdmin extends Admin {

    protected $baseRouteName = 'generalinformation';
    protected $baseRoutePattern = 'generalinformation';
    protected $childAssociationMapping = 'coordinate';

    // Fields to be shown on create/edit forms
    protected function configureFormFields(FormMapper $formMapper) {
        $formMapper
                ->add('geinCenterName', 'text', array('label' => 'Nombre del Centro'))
                ->add('geinAmenities', 'text', array('label' => 'Amenities'))
                ->add('geinMaximumHeight', 'text', array('label' => 'Altura Máxima'))
                ->add('geinMinimumHeight', 'text', array('label' => 'Altura Minima'))
                ->add('geinSeasonSince', 'text', array('label' => 'Comienzo de Temporada'))
                ->add('geinSeasonTill', 'text', array('label' => 'Fin de Temporada'))
                ->add('geinLocation', 'text', array('label' => 'Ubicación'))
                ->add('geinX', null, array('label' => 'Latitud'))
                ->add('geinY', null, array('label' => 'Longitud'))
                ->add('geinDetails', 'textarea', array('label' => 'Más Detalles'))
                ->add('Schedules', null, array('label' => 'Horarios'))
                ->add('Slopes', null, array('label' => 'Pistas'));
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

    protected function configureRoutes(RouteCollection $collection) {
        // to remove a single route
        // $collection->remove('delete');
        // OR remove all route except named ones
        $collection->clearExcept(array('edit', 'show'));
    }
    
    public function __toString() {
        return 'Información General';
    }

}
