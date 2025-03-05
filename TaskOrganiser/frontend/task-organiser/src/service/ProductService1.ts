import { ApiService } from './ApiService';

export interface Product {
    id?: string;
    code?: string;
    name: string;
    description: string;
    price: number;
    quantity?: number;
    inventoryStatus?: string;
    category?: string;
    image?: string;
    rating?: number;
}

export class ProductService1 {
    private apiService: ApiService;

    constructor() {
        this.apiService = new ApiService();
    }

    async getProductsSmall(): Promise<Product[]> {
        const response = await this.apiService.get<Product[]>('/products-small');
        return response.data;
    }

    public async getProducts(): Promise<Product[]> {
        const response = await this.apiService.get<Product[]>('/products');
        return response.data;
    }

    async getProductsMixed(): Promise<Product[]> {
        const response = await this.apiService.get<Product[]>('/products-mixed');
        return response.data;
    }
}

export default new ProductService1();
