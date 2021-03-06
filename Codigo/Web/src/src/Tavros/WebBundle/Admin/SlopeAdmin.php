<?php

namespace Tavros\WebBundle\Admin;

use Sonata\AdminBundle\Admin\Admin;
use Sonata\AdminBundle\Datagrid\ListMapper;
use Sonata\AdminBundle\Datagrid\DatagridMapper;
use Sonata\AdminBundle\Form\FormMapper;

class SlopeAdmin extends Admin {

    protected $baseRouteName = 'slope';
    protected $baseRoutePattern = 'slope';

    // Fields to be shown on create/edit forms
    protected function configureFormFields(FormMapper $formMapper) {
        $formMapper
        ->add('slopDescription', 'text', array('label' => 'Descripción'))
        ->add('slopLength', 'text', array('label' => 'Longitud'))
        ->add('slopDificulty', 'sonata_type_model', array('label' => 'Pistas', 'multiple' => false, 'by_reference' => false, "attr" => array("class" => "form-control")))
//        ->add('Coordinates', 'sonata_type_model',
//        array(
//        'by_reference' => false,
//        'multiple' => true,
//        'expanded' => true,
//        ));

//        ->add('Coordinates', 'sonata_type_model_hidden');
//                ->add('slopDificulty', null, array('label' => 'Dificultad', "attr" => array("class" => "form-control")));
    ;}

    // Fields to be shown on filter forms
    protected function configureDatagridFilters(DatagridMapper $datagridMapper) {
        $datagridMapper
                ->add('slopDescription', null, Array('label' => 'Descripción', 'sortable' => false))
                ->add('slopLength', null, Array('label' => 'Longitud', 'sortable' => false))
                ->add('slopDificulty', null, Array('label' => 'Dificultad', 'sortable' => false, "attr" => array("class" => "form-control")));
    }

    // Fields to be shown on lists
    protected function configureListFields(ListMapper $listMapper) {
        $listMapper
                ->addIdentifier('slopDescription', null, Array('label' => 'Descripción', 'sortable' => false))
                ->add('slopLength', null, Array('label' => 'Longitud', 'sortable' => false))
                ->add('slopDificulty', null, Array('label' => 'Dificultad', 'sortable' => false, "editable" => false, "attr" => array("class" => "form-control")));
    }

}
