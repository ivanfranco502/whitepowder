<?php

namespace Tavros\WebBundle\Admin;

use Sonata\AdminBundle\Admin\Admin;
use Sonata\AdminBundle\Datagrid\ListMapper;
use Sonata\AdminBundle\Datagrid\DatagridMapper;
use Sonata\AdminBundle\Form\FormMapper;

class HourDayAdmin extends Admin {

    protected $baseRouteName = 'hourday';
    protected $baseRoutePattern = 'hourday';

    // Fields to be shown on create/edit forms
    protected function configureFormFields(FormMapper $formMapper) {
        $formMapper
                ->add('hodaDay', 'text', array('label' => 'Day'))
                ->add('hodaStartHour', 'text', array('label' => 'Start Hour'))
                ->add('hodaEndHour', 'text', array('label' => 'End Hour'))
                ->add('hodaClose', null, array( 'label' => 'Close'));
    }

    // Fields to be shown on filter forms
    protected function configureDatagridFilters(DatagridMapper $datagridMapper) {
        $datagridMapper
                ->add('hodaDay')
                ->add('hodaStartHour')
                ->add('hodaEndHour')
                ->add('hodaClose');
    }

    // Fields to be shown on lists
    protected function configureListFields(ListMapper $listMapper) {
        $listMapper
                ->add('hodaDay', null, array('name' => 'Day'))
                ->add('hodaStartHour', null, array('name' => 'Start at'))
                ->add('hodaEndHour', null, array('name' => 'End at'))
                ->add('hodaClose', null, array('name' => 'Close'));
    }

}
