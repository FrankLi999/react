export interface ConfigurationModel {
    application: string;
    profile: string;
    label: string;
    key: string;
    props: ConfigurationProperty[];
}

export interface ConfigurationProperty {
    id: number;
    propKey: string;
    propValue: string;
}