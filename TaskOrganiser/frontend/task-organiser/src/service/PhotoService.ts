import { ApiService } from './ApiService';

export interface Photo {
    itemImageSrc?: string;
    thumbnailImageSrc?: string;
    alt?: string;
    title?: string;
}

class PhotoService {
    private apiService: ApiService;

    constructor() {
        this.apiService = new ApiService();
    }

    async getImages(): Promise<Photo[]> {
        const response = await this.apiService.get<Photo[]>('/photos');
        return response.data;
    }
}

export default new PhotoService();