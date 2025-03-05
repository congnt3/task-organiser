export interface LayoutConfig {
    ripple: boolean;
    darkTheme: boolean;
    inputStyle: string;
    menuMode: string;
    theme: string;
    scale: number;
    activeMenuItem: string | null;
}

export interface LayoutState {
    staticMenuDesktopInactive: boolean;
    overlayMenuActive: boolean;
    profileSidebarVisible: boolean;
    configSidebarVisible: boolean;
    staticMenuMobileActive: boolean;
    menuHoverActive: boolean;
}