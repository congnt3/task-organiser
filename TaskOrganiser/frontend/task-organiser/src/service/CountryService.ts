import { ApiService } from './ApiService';

export interface Country {
    name: string;
    code: string;
}

class CountryService {
    private apiService: ApiService;

    constructor() {
        this.apiService = new ApiService();
    }

    async getCountries(): Promise<Country[]> {
        const response = await this.apiService.get<Country[]>('/countries');
        return response.data;
    }
}

export default new CountryService();