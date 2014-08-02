<?php

namespace Tavros\WebBundle\Admin;

use Sonata\AdminBundle\Admin\Admin;
use Sonata\AdminBundle\Datagrid\ListMapper;
use Sonata\AdminBundle\Datagrid\DatagridMapper;
use Sonata\AdminBundle\Form\FormMapper;

class SlopeDificultyAdmin extends Admin {

    protected $baseRouteName = 'slopedificulty';
    protected $baseRoutePattern = 'dificulty';

    // Fields to be shown on create/edit forms
    protected function configureFormFields(FormMapper $formMapper) {
        $formMapper
                ->add('sldiDescription', 'text', array('label' => 'Description'))
                ->add('sldiColor', 'text', array('label' => 'Color'));
    }

    // Fields to be shown on filter forms
    protected function configureDatagridFilters(DatagridMapper $datagridMapper) {
        $datagridMapper
                ->add('sldiDescription')
                ->add('sldiColor');
    }

    // Fields to be shown on lists
    protected function configureListFields(ListMapper $listMapper) {
        $listMapper
                ->add('sldiDescription')
                ->add('sldiColor');
    }

}
