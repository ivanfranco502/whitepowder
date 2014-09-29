<?php

namespace Tavros\WebBundle\Admin;

use Sonata\AdminBundle\Admin\Admin;
use Sonata\AdminBundle\Datagrid\ListMapper;
use Sonata\AdminBundle\Datagrid\DatagridMapper;
use Sonata\AdminBundle\Form\FormMapper;
use Sonata\AdminBundle\Route\RouteCollection;

class HourDayAdmin extends Admin {

    protected $baseRouteName = 'hourday';
    protected $baseRoutePattern = 'hourday';

    // Fields to be shown on create/edit forms
    protected function configureFormFields(FormMapper $formMapper) {
        $formMapper
                ->add('hodaDay', 'text', array('label' => 'Día'))
                ->add('hodaStartHour', 'text', array('label' => 'Apertura'))
                ->add('hodaEndHour', 'text', array('label' => 'Cierre'))
                ->add('hodaClose', null, array('label' => 'Cerrado'));
    }

    // Fields to be shown on filter forms
//    protected function configureDatagridFilters(DatagridMapper $datagridMapper) {
//        $datagridMapper
//                ->add('hodaDay', null, array('name' => 'Día'))
//                ->add('hodaStartHour', null, array('name' => 'Apertura'))
//                ->add('hodaEndHour', null, array('name' => 'Cierre'))
//                ->add('hodaClose', null, array('name' => 'Cerrado'));
//    }

    // Fields to be shown on lists
    protected function configureListFields(ListMapper $listMapper) {
        $listMapper
                ->addIdentifier("hodaDay", null, Array('label'=>'Día', 'sortable' => false))
                ->add('hodaStartHour', null, array('label' => 'Apertura', 'sortable' => false))
                ->add('hodaEndHour', null, array('label' => 'Cierre', 'sortable' => false))
                ->add('hodaClose', null, array('label' => 'Cerrado', 'sortable' => false));
    }

    protected function configureRoutes(RouteCollection $collection) {
        // to remove a single route
        // $collection->remove('delete');
        // OR remove all route except named ones
        $collection->clearExcept(array('edit', 'show', 'list'));
    }

}
