export interface ConfigurationModel {
    application: string;
    profile: string;
    label: string;
    key: string;
    props: ConfigurationProperty[];
}

export interface ConfigurationProperty {
    propKey: string;
    propValue: string;
}

export interface DeleteModalProp {
  showModal: boolean;
  hideModal: any;
  confirmModal: any;
  row: any;
  message: any;
}


export interface ImportModalProp {
    showModal: boolean;
    hideModal: any;
    importConfiguration: any;
  }