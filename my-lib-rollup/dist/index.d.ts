import * as react_jsx_runtime from 'react/jsx-runtime';
import React from 'react';

interface SampleButtonProps {
    /**
     * Is this the principal call to action on the page?
     */
    primary?: boolean;
    /**
     * What background color to use
     */
    backgroundColor?: string;
    /**
     * How large should the button be?
     */
    size?: 'small' | 'medium' | 'large';
    /**
     * Button contents
     */
    label: string;
    /**
     * Optional click handler
     */
    onClick?: () => void;
}
/**
 * Primary UI component for user interaction
 */
declare const SampleButton: ({ primary, size, backgroundColor, label, ...props }: SampleButtonProps) => react_jsx_runtime.JSX.Element;

type SampleUser = {
    name: string;
};
interface SampleHeaderProps {
    user?: SampleUser;
    onLogin?: () => void;
    onLogout?: () => void;
    onCreateAccount?: () => void;
}
declare const SampleHeader: ({ user, onLogin, onLogout, onCreateAccount, }: SampleHeaderProps) => react_jsx_runtime.JSX.Element;

declare const SamplePage: React.FC;

export { SampleButton, type SampleButtonProps, SampleHeader, type SampleHeaderProps, SamplePage };
