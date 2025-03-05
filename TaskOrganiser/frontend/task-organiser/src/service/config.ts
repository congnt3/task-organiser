export interface Config {
    ripple: boolean;
    inputStyle: string;
    menuMode: string;
    theme: string;
    scale: number;
}

const config: Config = {
    ripple: false,
    inputStyle: 'outlined',
    menuMode: 'static',
    theme: 'lara-light-teal',
    scale: 14
};

export default config;