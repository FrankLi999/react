import { ConfigurationProperty } from "./ConfigurationProperty";

export interface ConfigData {
    key: string;
    application: string;
    profile: string;
    label: string;
    props: ConfigurationProperty[];
}
