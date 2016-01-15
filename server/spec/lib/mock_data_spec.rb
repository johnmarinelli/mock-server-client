require_relative '../spec_helper' 

describe 'mock data' do

  def mock_data_generator
    MockDataGenerator.new
  end

  it 'gets some mock data' do
    d = mock_data_generator.get_mocks(3)
    first = d[0]
    expect(d.count).to eq(3)
    expect(first[:name]).not_to eq(nil)
    expect(first[:email]).not_to eq(nil)
    expect(first[:ccn]).not_to eq(nil)
    expect(first[:profile_image]).not_to eq(nil)
    expect(first[:age]).not_to eq(nil)
    expect(first[:bio]).not_to eq(nil)
    expect(first[:created_at]).not_to eq(nil)
  end
end
