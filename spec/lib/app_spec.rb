require_relative '../spec_helper' 
require_relative '../../app/app'
require 'rack/test'

describe 'mock server app' do
  include Rack::Test::Methods

  def app
    MockServerApp.new
  end

  it 'returns some json' do
    get '/'
    expect(last_response.status).to eq(200)
    expect(last_response.content_type).to eq('application/json')
  end
end
